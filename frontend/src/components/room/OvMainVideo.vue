<template>
	<video autoplay/>
</template>

<script>
export default {
	name: 'OvMainVideo',

	props: {
		streamManager: Object,
	},

	mounted () {
		this.streamManager.addVideoElement(this.$el);
	},
};
</script>

<style scoped>
video {
  width: 859px;
  /* border-radius: 10px; */
  padding: 70px 0px;
  background-color: #D3E2E7;
}
</style>